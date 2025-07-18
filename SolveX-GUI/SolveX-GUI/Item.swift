//
//  Item.swift
//  SolveX-GUI
//
//  Created by Will Charlton on 18/07/2025.
//

import Foundation
import SwiftData

@Model
final class Item {
    var timestamp: Date
    
    init(timestamp: Date) {
        self.timestamp = timestamp
    }
}
