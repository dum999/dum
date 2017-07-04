using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class PlayControl : MonoBehaviour {

	private Rigidbody rb;
	public float speed;
	private int count;

	public Text countText;

	// Use this for initialization
	void Start () {
		count = 0;
		rb = GetComponent<Rigidbody> ();
	}
	
	// Update is called once per frame
	void Update () {
		float z = Input.GetAxis ("Vertical") * Time.deltaTime * speed;
		float x = Input.GetAxis ("Horizontal") * Time.deltaTime * speed;

		Vector3 movement = new Vector3 (x, 0, z);

		rb.AddForce (movement);
	}

	void OnTriggerEnter(Collider other) {
		if (other.gameObject.CompareTag ("Box")) {
			other.gameObject.SetActive (false);
			count++;
			countText.text = "Score: " + count;
		}
	}
}
