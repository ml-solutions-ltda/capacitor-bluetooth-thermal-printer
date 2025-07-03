package br.net.mlsolutions.app.printthermal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.getcapacitor.PluginCall;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.UUID;
import com.getcapacitor.JSObject;
import com.getcapacitor.JSArray;
import java.util.Set;

import android.os.Handler;
import android.os.Looper;

import com.getcapacitor.PluginCall;

public class PrintThermal {
    private final Context context;

    public PrintThermal(Context ctx) {
        this.context = ctx;
    }

    public JSObject listPrinters(final PluginCall call) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            call.reject("Bluetooth não suportado");
            return null;
        }

        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
        JSArray printers = new JSArray();

        for (BluetoothDevice device : pairedDevices) {
            JSObject item = new JSObject();
            item.put("name", device.getName());
            item.put("address", device.getAddress());
            printers.put(item);
        }

        JSObject result = new JSObject();
        result.put("devices", printers);
        return result;
    }

    public void printHtml(final String html, final String macAddress, final String logoBase64, final String qrCodeText, final PluginCall call) {
        Handler mainHandler = new Handler(Looper.getMainLooper());

        mainHandler.post(() -> {
            final WebView webView = new WebView(context);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.layout(0, 0, 384, 6000);
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);

            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    webView.measure(View.MeasureSpec.makeMeasureSpec(384, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    webView.layout(0, 0, 384, webView.getMeasuredHeight());
                    Bitmap bodyBitmap = Bitmap.createBitmap(webView.getMeasuredWidth(), webView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bodyBitmap);
                    webView.draw(canvas);

                    Bitmap finalBitmap = composePrintBitmap(logoBase64, bodyBitmap, qrCodeText);
                    sendToPrinter(macAddress, finalBitmap, call);
                }
            });
        });
    }


    private Bitmap composePrintBitmap(String logoBase64, Bitmap body, String qrText) {
        Bitmap logo = decodeBase64Image(logoBase64);
        Bitmap qrCode = (qrText != null) ? generateQRCode(qrText) : null;

        int width = 384;
        int totalHeight = (logo != null ? logo.getHeight() : 0) + body.getHeight() + (qrCode != null ? qrCode.getHeight() : 0);
        Bitmap result = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        int y = 0;

        if (logo != null) {
            canvas.drawBitmap(logo, (width - logo.getWidth()) / 2f, y, new Paint());
            y += logo.getHeight();
        }

        canvas.drawBitmap(body, 0, y, new Paint());
        y += body.getHeight();

        if (qrCode != null) {
            canvas.drawBitmap(qrCode, (width - qrCode.getWidth()) / 2f, y, new Paint());
        }

        return result;
    }

    private Bitmap decodeBase64Image(String base64Str) {
        if (base64Str == null || !base64Str.contains("base64,")) return null;
        String base64Image = base64Str.substring(base64Str.indexOf("base64,") + 7);
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private Bitmap generateQRCode(String data) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 256, 256);
            Bitmap bmp = Bitmap.createBitmap(256, 256, Bitmap.Config.RGB_565);
            for (int x = 0; x < 256; x++) {
                for (int y = 0; y < 256; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? android.graphics.Color.BLACK : android.graphics.Color.WHITE);
                }
            }
            return bmp;
        } catch (Exception e) {
            return null;
        }
    }

    private void sendToPrinter(String mac, Bitmap image, PluginCall call) {
        try {
            BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mac);
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.randomUUID());
            socket.connect();

            OutputStream stream = socket.getOutputStream();
            byte[] bytes = convertBitmapToESCPOS(image);
            stream.write(bytes);
            stream.flush();package br.net.mlsolutions.app.printthermal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

            public class PrintThermal {
                private final Context context;

                public PrintThermal(Context ctx) {
                    this.context = ctx;
                }

                public JSObject listPrinters(final PluginCall call) {
                    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                    if (adapter == null) {
                        call.reject("Bluetooth não suportado");
                        return null;
                    }

                    Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
                    JSArray printers = new JSArray();

                    for (BluetoothDevice device : pairedDevices) {
                        JSObject item = new JSObject();
                        item.put("name", device.getName());
                        item.put("address", device.getAddress());
                        printers.put(item);
                    }

                    JSObject result = new JSObject();
                    result.put("devices", printers);
                    return result;
                }

                public void printHtml(final String html, final String macAddress, final String logoBase64, final String qrCodeText, final PluginCall call) {
                    Handler mainHandler = new Handler(Looper.getMainLooper());

                    mainHandler.post(() -> {
                        final WebView webView = new WebView(context);
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.layout(0, 0, 384, 6000);
                        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);

                        webView.setWebViewClient(new WebViewClient() {
                            public void onPageFinished(WebView view, String url) {
                                webView.measure(
                                        View.MeasureSpec.makeMeasureSpec(384, View.MeasureSpec.EXACTLY),
                                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                                );
                                webView.layout(0, 0, 384, webView.getMeasuredHeight());

                                Bitmap bodyBitmap = Bitmap.createBitmap(
                                        webView.getMeasuredWidth(),
                                        webView.getMeasuredHeight(),
                                        Bitmap.Config.ARGB_8888
                                );
                                Canvas canvas = new Canvas(bodyBitmap);
                                webView.draw(canvas);

                                // Verifica se deve incluir logo e/ou QR
                                boolean hasLogo = logoBase64 != null && !logoBase64.trim().isEmpty();
                                boolean hasQrCode = qrCodeText != null && !qrCodeText.trim().isEmpty();

                                Bitmap finalBitmap = composePrintBitmap(
                                        hasLogo ? logoBase64 : null,
                                        bodyBitmap,
                                        hasQrCode ? qrCodeText : null
                                );

                                sendToPrinter(macAddress, finalBitmap, call);
                            }
                        });
                    });
                }

                private Bitmap composePrintBitmap(String logoBase64, Bitmap body, String qrText) {
                    Bitmap logo = decodeBase64Image(logoBase64);
                    Bitmap qrCode = (qrText != null) ? generateQRCode(qrText) : null;

                    int width = 384;
                    int totalHeight = (logo != null ? logo.getHeight() : 0)
                            + body.getHeight()
                            + (qrCode != null ? qrCode.getHeight() : 0);

                    Bitmap result = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(result);
                    int y = 0;

                    if (logo != null) {
                        canvas.drawBitmap(logo, (width - logo.getWidth()) / 2f, y, new Paint());
                        y += logo.getHeight();
                    }

                    canvas.drawBitmap(body, 0, y, new Paint());
                    y += body.getHeight();

                    if (qrCode != null) {
                        canvas.drawBitmap(qrCode, (width - qrCode.getWidth()) / 2f, y, new Paint());
                    }

                    return result;
                }

                private Bitmap decodeBase64Image(String base64Str) {
                    if (base64Str == null || !base64Str.contains("base64,")) return null;
                    try {
                        String base64Image = base64Str.substring(base64Str.indexOf("base64,") + 7);
                        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                        return android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    } catch (Exception e) {
                        return null;
                    }
                }

                private Bitmap generateQRCode(String data) {
                    try {
                        BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 256, 256);
                        Bitmap bmp = Bitmap.createBitmap(256, 256, Bitmap.Config.RGB_565);
                        for (int x = 0; x < 256; x++) {
                            for (int y = 0; y < 256; y++) {
                                bmp.setPixel(x, y, bitMatrix.get(x, y) ? android.graphics.Color.BLACK : android.graphics.Color.WHITE);
                            }
                        }
                        return bmp;
                    } catch (Exception e) {
                        return null;
                    }
                }

                private void sendToPrinter(String mac, Bitmap image, PluginCall call) {
                    try {
                        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mac);
                        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.randomUUID());
                        socket.connect();

                        OutputStream stream = socket.getOutputStream();
                        byte[] bytes = convertBitmapToESCPOS(image);
                        stream.write(bytes);
                        stream.flush();
                        stream.close();
                        socket.close();

                        call.resolve();
                    } catch (Exception e) {
                        call.reject("Erro ao imprimir: " + e.getMessage());
                    }
                }

                private byte[] convertBitmapToESCPOS(Bitmap bitmap) {
                    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
                    return byteStream.toByteArray();
                }
            }

            stream.close();
            socket.close();
            call.resolve();
        } catch (Exception e) {
            call.reject("Erro ao imprimir: " + e.getMessage());
        }
    }

    private byte[] convertBitmapToESCPOS(Bitmap bitmap) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return byteStream.toByteArray();
    }
}
