using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class GlobalCookies : MonoBehaviour
{

    public static int dpCount;
    public TextMeshProUGUI dpDisplay;
    public int InternalDP;

    void Update()
    {
        InternalDP = dpCount;
        dpDisplay.text = "Destruction Points: " + InternalDP;
        
    }
}
