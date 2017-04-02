//
//  TableViewCellStyleViewController.swift
//  MyProject
//
//  Created by Jirapong Charoentim on 4/2/2560 BE.
//  Copyright © 2560 Jirapong Charoentim. All rights reserved.
//

import UIKit

class TableViewCellStyleViewController: UITableViewController {
    
    private let cellIdentifiers = [ "BasicCell", "RightDetailCell", "LeftDetailCell", "SubtitleCell"]

    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return cellIdentifiers.count
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 3
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifiers[indexPath.section], for: indexPath)

        cell.textLabel?.text = "Title \(indexPath.section) Row \(indexPath.row)"
        if let detailTextLabel = cell.detailTextLabel {
            detailTextLabel.text = "Detail \(indexPath.section) Row \(indexPath.row)"
        }
        if let imageView = cell.imageView {
            imageView.image = UIImage(named: "fruit\(indexPath.row + 1)")
        }
        
        return cell
    }
    
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return cellIdentifiers[section]
    }
}
