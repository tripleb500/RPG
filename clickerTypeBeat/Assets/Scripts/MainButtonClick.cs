using UnityEngine;

public class MainButtonClick : MonoBehaviour
{
    public GameObject textBox;
    public AudioSource destroySound;

    public void ClickTheButton()
    {
        destroySound.Play();
        GlobalCookies.dpCount += 1;
    }
    
}
