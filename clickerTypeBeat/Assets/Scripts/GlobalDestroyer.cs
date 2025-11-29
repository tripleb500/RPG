using UnityEngine;
using TMPro;

public class GlobalDestroyer : MonoBehaviour {

    public GameObject fakeButton;
    public TextMeshProUGUI FakeText;
    public GameObject realButton;
    public TextMeshProUGUI RealText;
    public int currentPoints;
    public static int destroyerValue = 10;
    public static bool turnOffButton = false;
    public TextMeshProUGUI destroyerStats;
    public static int numberOfDestroyers;
    public static int destroyPerSec;

    void Update()
    {
        currentPoints = GlobalCookies.dpCount;
        destroyerStats.text = "Destroyers: " + numberOfDestroyers + " @ " + destroyPerSec + " Per Second";
        FakeText.text = "Buy Destroyer - " + destroyerValue + " DP";
        RealText.text = "Buy Destroyer - " + destroyerValue + " DP";
        if (currentPoints >= destroyerValue )
        {
            fakeButton.SetActive( false );
            realButton.SetActive( true );
        }

        if (turnOffButton == true)
        {
            realButton.SetActive( false );
            fakeButton.SetActive( true );
            turnOffButton = false;

        }

    }

}
