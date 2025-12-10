using TMPro;
using UnityEngine;
using System;
using Unity;
using UnityEngine.UI;

public class DataFromApp : MonoBehaviour
{
    public TextMeshProUGUI userNameText;
    public TextMeshProUGUI userLevelText;

    void Start()
    {
#if UNITY_ANDROID
        try
        {
            using (var unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer"))
            {
                var activity = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
                var intent = activity.Call<AndroidJavaObject>("getIntent");

                bool hasName = intent.Call<bool>("hasExtra", "userName");
                bool hasLevel = intent.Call<bool>("hasExtra", "userLevel");

                Debug.Log($"[IntentReader] hasName={hasName}, hasLevel={hasLevel}");

                string userName = hasName
                    ? intent.Call<string>("getStringExtra", "userName")
                    : "No name extra";

                int userLevel = hasLevel
                    ? intent.Call<int>("getIntExtra", "userLevel", 0)
                    : 0;

                Debug.Log($"[IntentReader] userName={userName}, userLevel={userLevel}");

                if (userNameText != null)
                    userNameText.text = userName;

                if (userLevelText != null)
                    userLevelText.text = userLevel.ToString();
            }
        }
        catch (System.Exception e)
        {
            Debug.LogError("[IntentReader] Error reading intent: " + e);
        }
#endif
    }
}
//    private bool getIntentData()
//    {
//    #if (!UNITY_EDITOR && UNITY_ANDROID)
//        return CreatePushClass (new AndroidJavaClass ("com.unity3d.player.UnityPlayer"));
//    #endif
//        return false;
//    }

//    public bool CreatePushClass(AndroidJavaClass UnityPlayer)
//    {
//#if UNITY_ANDROID
//        AndroidJavaObject currentActivity = UnityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
//        AndroidJavaObject intent = currentActivity.Call<AndroidJavaObject>("getIntent");
//        AndroidJavaObject extras = GetExtras(intent);

//        if (extras != null)
//        {
//            string ex = GetProperty(extras, "my_text");
//            return true;
//        }
//#endif
//        return false;
//    }

//    private AndroidJavaObject GetExtras(AndroidJavaObject intent)
//    {
//        AndroidJavaObject extras = null;

//        try
//        {
//            extras = intent.Call<AndroidJavaObject>("getExtras");
//        }
//        catch (Exception e)
//        {
//            Debug.Log(e.Message);
//        }

//        return extras;
//    }

//    private string GetProperty(AndroidJavaObject extras, string name)
//    {
//        string s = string.Empty;

//        try
//        {
//            s = extras.Call<string>("getString", name);
//        }
//        catch (Exception e)
//        {
//            Debug.Log(e.Message);
//        }

//        user.text = s;
//        return s;
//    }

