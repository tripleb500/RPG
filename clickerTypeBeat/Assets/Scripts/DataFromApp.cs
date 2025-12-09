using TMPro;
using UnityEngine;
using System;

public class DataFromApp : MonoBehaviour
{
    public TextMeshProUGUI user;

    private void Awake()
    {
        getIntentData();
    }

    private bool getIntentData()
    {
    #if (!UNITY_EDITOR && UNITY_ANDROID)
        return CreatePushClass (new AndroidJavaClass ("com.unity3d.player.UnityPlayer"));
    #endif
        return false;
    }

    public bool CreatePushClass(AndroidJavaClass UnityPlayer)
    {
#if UNITY_ANDROID
        AndroidJavaObject currentActivity = UnityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
        AndroidJavaObject intent = currentActivity.Call<AndroidJavaObject>("getIntent");
        AndroidJavaObject extras = GetExtras(intent);

        if (extras != null)
        {
            string ex = GetProperty(extras, "my_text");
            return true;
        }
#endif
        return false;
    }

    private AndroidJavaObject GetExtras(AndroidJavaObject intent)
    {
        AndroidJavaObject extras = null;

        try
        {
            extras = intent.Call<AndroidJavaObject>("getExtras");
        }
        catch (Exception e)
        {
            Debug.Log(e.Message);
        }

        return extras;
    }

    private string GetProperty(AndroidJavaObject extras, string name)
    {
        string s = string.Empty;

        try
        {
            s = extras.Call<string>("getString", name);
        }
        catch (Exception e)
        {
            Debug.Log(e.Message);
        }

        user.text = s;
        return s;
    }


}
