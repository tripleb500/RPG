using System.Collections;
using TMPro;
using UnityEngine;

public class MiracleScript : MonoBehaviour {
    public TextMeshProUGUI statusBox;
    public float dpCheck;
    public int genChance;
    public bool miracleActive = false;
    public int dpGained;

    private void Update()
    {
        dpCheck = GlobalCookies.dpCount / 10;
        if (miracleActive == false)
        {
            StartCoroutine(StartMiracle());
        }
    }

    IEnumerator StartMiracle()
    {
        miracleActive = true;
        genChance = Random.Range(1, 20);
        if (dpCheck >= genChance)
        {
            dpGained = Mathf.RoundToInt(GlobalCookies.dpCount * 0.15f);
            statusBox.text = "You gained " + dpGained + " from the meteor losing some mass";
            GlobalCookies.dpCount += dpGained;
            yield return new WaitForSeconds(3);
            statusBox.GetComponent<Animation>().Play("StatusAnim");
            yield return new WaitForSeconds(1);
            statusBox.enabled = false;
            statusBox.enabled = true;

        }
        yield return new WaitForSeconds(5);
        miracleActive = false;

    }

}
