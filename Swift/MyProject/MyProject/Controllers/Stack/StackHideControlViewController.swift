//
//  StackHideControlViewController.swift
//  MyProject
//
//  Created by Jirapong Charoentim on 4/2/2560 BE.
//  Copyright © 2560 Jirapong Charoentim. All rights reserved.
//

import UIKit

class StackHideControlViewController: UIViewController {

    @IBOutlet var backgroundColoredViews: [UIView]!
    @IBOutlet var headingLabels: [UILabel]!
    
    @IBOutlet weak var whyVisitLabel: UILabel!
    @IBOutlet weak var whatToSeeLabel: UILabel!
    @IBOutlet weak var weatherInfoLabel: UILabel!
    @IBOutlet weak var userRatingLabel: UILabel!
    @IBOutlet weak var weatherHideOrShowButton: UIButton!
    
    private let vacationSpots = VacationSpot.loadAllVacationSpots()
    
    private var shouldHideWeatherInfoSetting: Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Clear background colors from labels and buttons
        for view in backgroundColoredViews {
            view.backgroundColor = UIColor.clear
        }
        
        // Set the kerning to 1 to increase spacing between letters
        headingLabels.forEach { $0.attributedText = NSAttributedString(string: $0.text!, attributes: [NSKernAttributeName: 1]) }
        
        whyVisitLabel.text = vacationSpots[0].whyVisit
        whatToSeeLabel.text = vacationSpots[0].whatToSee
        weatherInfoLabel.text = vacationSpots[0].weatherInfo
        userRatingLabel.text = String(repeating: "★", count: vacationSpots[0].userRating)
     
        updateWeatherInfoViews(hideWeatherInfo: shouldHideWeatherInfoSetting, animated: false)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func weatherHideOrShowButtonTapped(_ sender: UIButton) {
        let shouldHideWeatherInfo = sender.titleLabel!.text! == "Hide"
        updateWeatherInfoViews(hideWeatherInfo: shouldHideWeatherInfo, animated: true)
        shouldHideWeatherInfoSetting = shouldHideWeatherInfo
    }
    
    func updateWeatherInfoViews(hideWeatherInfo shouldHideWeatherInfo: Bool, animated: Bool) {
        let newButtonTitle = shouldHideWeatherInfo ? "Show" : "Hide"
        weatherHideOrShowButton.setTitle(newButtonTitle, for: UIControlState())
        
        if animated {
            UIView.animate(withDuration: 0.3) {
                self.weatherInfoLabel.isHidden = shouldHideWeatherInfo
            }
        } else {
            weatherInfoLabel.isHidden = shouldHideWeatherInfo
        }
    }

}
