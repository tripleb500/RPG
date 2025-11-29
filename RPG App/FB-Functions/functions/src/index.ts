/**
 * Import function triggers from their respective submodules:
 *
 * import {onCall} from "firebase-functions/v2/https";
 * import {onDocumentWritten} from "firebase-functions/v2/firestore";
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

import { setGlobalOptions } from "firebase-functions";
//import { onRequest } from "firebase-functions/https";
//import * as logger from "firebase-functions/logger";

// Start writing functions
// https://firebase.google.com/docs/functions/typescript

// For cost control, you can set the maximum number of containers that can be
// running at the same time. This helps mitigate the impact of unexpected
// traffic spikes by instead downgrading performance. This limit is a
// per-function limit. You can override the limit for each function using the
// `maxInstances` option in the function's options, e.g.
// `onRequest({ maxInstances: 5 }, (req, res) => { ... })`.
// NOTE: setGlobalOptions does not apply to functions using the v1 API. V1
// functions should each use functions.runWith({ maxInstances: 10 }) instead.
// In the v1 API, each function can only serve one request per container, so
// this will be the maximum concurrent request count.
setGlobalOptions({ maxInstances: 10 });

// export const helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });

/**
 * Note for RPG TEAM [How to deploy firebase functions from terminal] 
 * 1. firebase login
 * 2. now cd to root folder (you'll see firebase.json)
 * 3. firebase deploy
 * 4. or firebase deploy --only functions
 */
import * as functions from 'firebase-functions/v1';
import * as admin from "firebase-admin";

admin.initializeApp({
    credential: admin.credential.applicationDefault(),
    projectId: "rpg-database-project",
});
const db = admin.firestore();

//AUTOMATIC QUEST STATUS UPDATING

type RepeatType = "NONE" | "DAY" | "WEEK" | "MONTH" | "YEAR";

interface Quest {
    [key: string]: any;
    deadlineDate?: admin.firestore.Timestamp;
    repeat?: boolean;
    repeatType?: RepeatType;
    repeatInterval?: number;
    status?: string;
}

function computeNextDeadline(
    current: Date,
    repeatType: RepeatType,
    repeatInterval: number
): Date {
    //Input validation atleast 1, make copy of current date
    const step = Math.max(repeatInterval || 1, 1);
    const date = new Date(current.getTime());

    switch (repeatType) {
        case "DAY":
            date.setDate(date.getDate() + step);
            break;
        case "WEEK":
            date.setDate(date.getDate() + step * 7);
            break;
        case "MONTH":
            date.setMonth(date.getMonth() + step);
            break;
        case "YEAR":
            date.setFullYear(date.getFullYear() + step);
            break;
        case "NONE":
        default:
            break;
    }
    return date;
}

//On scheduled time: check quests based on deadline
//If deadline has passed, update and archive quest to no longer repeat then create new quest copy.
export const updateQuests = functions.pubsub
    //Cron how often updated
    .schedule("every 15 minutes")
    .timeZone("America/Los_Angeles")
    .onRun(async () => {
        const now = new Date();

        //Grab all quests with the following status
        const questCollection = await db.collection("quests")
            .where("status", "in", ["INPROGRESS", "PENDING", "COMPLETED", "INCOMPLETE"])
            .get();

        if (questCollection.empty) {
            console.log("No active quests to process.");
            return null;
        }

        //Hold all the update operations in here so we can do all the updating at once later
        const batch = db.batch();
        //Keep track of how many documents were updated/created
        let updated = 0;
        let created = 0;

        //Iterate thru quests
        questCollection.forEach((doc) => {
            const data = doc.data() as Quest;
            const {
                deadlineDate,
                repeat = false,
                repeatType = "NONE",
                repeatInterval = 1,
                status,
                ...rest
            } = data;

            //If no deadline skip
            if (!deadlineDate) return;

            const deadline = deadlineDate.toDate();

            //If the deadline hasn't passed by skip
            if (deadline > now) return;

            //Handle repeatable quests that have passed the deadline
            if (repeat && repeatType !== "NONE") {
                //Ensure completed quests don't change their status
                if (status === "COMPLETED") {
                    batch.update(doc.ref, {
                        repeat: false,
                        repeatType: "NONE",
                    });
                    updated++;
                } else {
                    //Every other status from above
                    batch.update(doc.ref, {
                        status: "INCOMPLETE",
                        repeat: false,
                        repeatType: "NONE"
                    });
                    updated++;
                }

                //Compute next deadline
                const next = computeNextDeadline(deadline, repeatType, repeatInterval);

                //Generate new quest with copied data except explicity defined fields
                const newRef = db.collection("quests").doc();
                const newQuest: any = {
                    ...rest,
                    repeat: true,
                    repeatType,
                    repeatInterval,
                    assignDate: admin.firestore.Timestamp.fromDate(now),
                    deadlineDate: admin.firestore.Timestamp.fromDate(next),
                    status: "INPROGRESS",
                    completionDate: null,
                };

                //Put current update operation in batch
                batch.set(newRef, newQuest);
                created++;
            }
            //If not repeatable and not already marked incomplete
            else if (!repeat && repeatType === "NONE" && status !== "INCOMPLETE" && status !== "COMPLETED") {
                batch.update(doc.ref, {
                    status: "INCOMPLETE",
                });
                updated++;
            }
        });

        //Update all at once
        if (updated > 0 || created > 0) {
            await batch.commit();
        }

        console.log(
            `processQuestsSchedule: updated=${updated}, created=${created}`
        );

        return null;
    });

//QUEST REMINDER NOTIFICATIONS

interface QuestReminder {
    questId: string;
    childId: string;
    parentId: string;
    notificationMessage: string;
    createdAt?: admin.firestore.Timestamp;
}

//When a reminder document is created, send a push notification
export const sendQuestReminder = functions.firestore
    .document("questReminders/{reminderId}")
    .onCreate(async (snap, context) => {
        console.log("sendQuestReminder TRIGGERED, doc ID:", snap.id);
        const data = snap.data() as QuestReminder;
        const { childId, questId, notificationMessage } = data;

        if (!childId) {
            console.log("No childId in reminder, skipping");
            return null;
        }

        //Get child's user doc
        const userDoc = await db.collection("users").doc(childId).get();
        if (!userDoc.exists) {
            console.log("Child not found:", childId);
            return null;
        }

        //Read doc for FCM Token
        const userData = userDoc.data() as { fcmToken?: string };
        const token = userData.fcmToken;

        //DO NOT USE THIS UNLESS NECESSARY
        //console.log("Token extracted:", token ?? "NULL");

        if (!token) {
            console.log("No fcmToken for child:", childId);
            return null;
        }

        //Notification payload, fallback to string if no message
        const message: admin.messaging.Message = {
            token: token,
            notification: {
                title: "Quest Reminder",
                body: notificationMessage || "Remember to do your quests!",
            },
            data: {
                questId: questId ?? "",
                type: "QUEST_REMINDER",
            },
        };

        try {
            const result = await admin.messaging().send(message);
            console.log("FCM send success:", result);
        } catch (error) {
            console.error("FCM send error:", error);
        }
        return null;
    });
