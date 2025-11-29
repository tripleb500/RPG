using System.Collections;
using UnityEngine;

public class AutoCookie : MonoBehaviour
{

    public bool CreatingDestroyer = false;
    public static int DestroyerIncrease = 1;
    public int InternalIncrease;

    private void Update()
    {
        DestroyerIncrease = GlobalDestroyer.destroyPerSec;
        InternalIncrease = DestroyerIncrease;
        if (CreatingDestroyer == false)
        {
            CreatingDestroyer = true;
            StartCoroutine(CreateTheDestroyer());
        }
    }

    IEnumerator CreateTheDestroyer()
    {
        GlobalCookies.dpCount += InternalIncrease;
        yield return new WaitForSeconds(1);
        CreatingDestroyer = false;
    }

}
