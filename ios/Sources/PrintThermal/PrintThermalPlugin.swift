import Capacitor
import Foundation

@objc(PrintThermalPlugin)
public class PrintThermalPlugin: CAPPlugin {

    @objc func print(_ call: CAPPluginCall) {
        guard let html = call.getString("html") else {
            call.reject("html obrigatório")
            return
        }

        let printer = PrintThermal(html: html)
        printer.printReceipt { success, errorMessage in
            if success {
                call.resolve()
            } else {
                call.reject(errorMessage ?? "Erro desconhecido ao imprimir")
            }
        }
    }

    @objc func listPrinters(_ call: CAPPluginCall) {
        // AirPrint não permite listagem real de dispositivos
        // Retornamos uma opção simbólica para fins de interface
        call.resolve([
            "devices": [
                [
                    "name": "AirPrint (iOS)",
                    "address": "airprint"
                ]
            ]
        ])
    }

}
