import UIKit
import WebKit

class PrintThermal {
    let html: String

    init(html: String) {
        self.html = html
    }

    func printReceipt(completion: @escaping (Bool, String?) -> Void) {
        DispatchQueue.main.async {
            let printFormatter = UIMarkupTextPrintFormatter(markupText: self.html)
            let printInfo = UIPrintInfo(dictionary: nil)
            printInfo.outputType = .general
            printInfo.jobName = "Comprovante"

            let printController = UIPrintInteractionController.shared
            printController.printInfo = printInfo
            printController.printFormatter = printFormatter

            printController.present(animated: true) { (controller, completed, error) in
                if completed && error == nil {
                    completion(true, nil)
                } else {
                    completion(false, error?.localizedDescription ?? "Impressão cancelada")
                }
            }
        }
    }
}
