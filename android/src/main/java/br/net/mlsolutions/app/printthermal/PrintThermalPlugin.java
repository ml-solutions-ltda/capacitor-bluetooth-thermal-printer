package br.net.mlsolutions.app.printthermal;

import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.Plugin;

import com.getcapacitor.JSObject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


@CapacitorPlugin(name = "PrintThermal")
public class PrintThermalPlugin extends Plugin {

    @PluginMethod
    public void listPrinters(PluginCall call) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1001);
                call.reject("BLUETOOTH_CONNECT permission is required");
                return;
            }
        }

        PrintThermal printer = new PrintThermal(getContext());
        JSObject result = printer.listPrinters(call);
        call.resolve(result);
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
