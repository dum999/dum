//
//  SwitchContainerViewController.swift
//  MyProject
//
//  Created by Jirapong Charoentim on 4/2/2560 BE.
//  Copyright © 2560 Jirapong Charoentim. All rights reserved.
//

import UIKit

class SwitchContainerViewController: UIViewController {

    @IBOutlet weak var container1: UIView!
    @IBOutlet weak var container2: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        container2.isHidden = true
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

    @IBAction func switchButtonClick(_ sender: Any) {
        container1.isHidden = !container1.isHidden
        container2.isHidden = !container2.isHidden

    }
}
