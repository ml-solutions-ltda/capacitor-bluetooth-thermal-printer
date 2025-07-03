package br.net.mlsolutions.app.printthermal;

import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.Plugin;

@CapacitorPlugin(name = "PrintThermal")
public class PrintThermalPlugin extends Plugin {

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");
        PrintThermal printer = new PrintThermal(getContext());
        String result = printer.echo(value);

        call.resolve(new JSObject().put("value", result));
    }

    @PluginMethod
    public void print(PluginCall call) {
        // Chamar sua lógica de impressão real aqui
        String html = call.getString("html");
        String macAddress = call.getString("macAddress");
        String logo = call.getString("logoBase64");
        String qr = call.getString("qrCodeText");

        PrintThermal printer = new PrintThermal(getContext());
        printer.printHtml(html, macAddress, logo, qr, call);
    }
}
