using UnityEngine;

public class PurchaseLog : MonoBehaviour 
{
    public GameObject AutoCookie;
    public AudioSource playSound;

    public void StartAutoCookie()
    {
        playSound.Play();
        AutoCookie.SetActive(true);
        GlobalCookies.dpCount -= GlobalDestroyer.destroyerValue;
        GlobalDestroyer.destroyerValue *= 2;
        GlobalDestroyer.turnOffButton = true;
        GlobalDestroyer.destroyPerSec += 1;
        GlobalDestroyer.numberOfDestroyers += 1;
    }

}
