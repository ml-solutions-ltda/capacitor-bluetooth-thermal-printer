# 🖨️ Capacitor Thermal Bluetooth Printer Plugin

Este plugin Capacitor permite impressão direta em **impressoras térmicas Bluetooth (58mm ou 80mm)** e **AirPrint no iOS**, usando conteúdo HTML renderizado e convertido em imagem. Inclui suporte a:

✅ Impressão de HTML
✅ Impressão de QR Code
✅ Inclusão de logotipo no topo
✅ Listagem de impressoras emparelhadas
✅ Comunicação direta via Bluetooth (Android) ou AirPrint (iOS)
✅ Sem necessidade de apps externos

---

## 📦 Instalação

```bash
npm install ./print-plugin
npx cap sync
```

---

## 🧠 Pré-requisitos

### Android

* `minSdkVersion`: 21+
* No `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
```

> ⚠️ Para Android 12+ (API 31+), solicite permissões `BLUETOOTH_CONNECT` e `BLUETOOTH_SCAN` em tempo de execução.

---

### iOS

* Suporta apenas impressoras **AirPrint**
* Não é necessário permissão extra
* A impressão abre o seletor nativo do sistema

---

## 🚀 Como Usar

### 1. Importar o plugin:

```ts
import PrintThermalPlugin from 'print-plugin';
```

### 2. Listar impressoras disponíveis:

```ts
const result = await PrintThermalPlugin.listPrinters();
console.log(result.devices);
// Android: [{ name: 'XP-58', address: '00:11:22:33:44:55' }]
// iOS:     [{ name: 'AirPrint (iOS)', address: 'airprint' }]
```

### 3. Imprimir conteúdo:

```ts
await PrintThermalPlugin.print({
  macAddress: selected.address, // no iOS, use 'airprint'
  html: '<h1>Pedido #123</h1><p>Total: R$ 25,00</p>',
  logoBase64: 'data:image/png;base64,...',   // opcional
  qrCodeText: 'https://meusite.com/pedido/123', // opcional
  paperWidthMm: 58                            // obrigatório no Android
});
```

---

## 🍏 iOS – AirPrint

No iOS, o plugin usa `UIPrintInteractionController` para abrir o seletor de impressão nativo.

### ✅ Como usar no iOS:

```ts
await PrintThermalPlugin.print({
  html: '<h2>Recibo</h2><p>Obrigado por comprar conosco!</p>',
  macAddress: 'airprint' // obrigatório usar este valor no iOS
});
```

> No iOS, os campos `logoBase64`, `qrCodeText` e `paperWidthMm` são ignorados, pois o conteúdo é renderizado pelo próprio motor de impressão do sistema.

---

## 🎨 Personalização

### ✅ `html`

Conteúdo HTML com estilos inline.

### ✅ `logoBase64` (opcional)

Imagem base64 (PNG, JPG...) que será posicionada no topo do recibo.

### ✅ `qrCodeText` (opcional)

Texto ou URL que será convertido automaticamente em QR Code.

### ✅ `paperWidthMm`

* `58`: impressoras compactas
* `80`: impressoras maiores (mais largas)
* **Obrigatório no Android**, ignorado no iOS

---

## 📸 Exemplo Completo

```ts
import PrintThermalPlugin from 'print-plugin';

async function imprimirPedido() {
  const printers = await PrintThermalPlugin.listPrinters();
  const selected = printers.devices[0];

  await PrintThermalPlugin.print({
    macAddress: selected.address,
    html: `
      <div style="text-align:center">
        <h1>Pedido #123</h1>
        <p>Produto A - R$ 10,00</p>
        <p>Produto B - R$ 15,00</p>
        <h3>Total: R$ 25,00</h3>
      </div>
    `,
    logoBase64: 'data:image/png;base64,...',
    qrCodeText: 'https://meusite.com/pedido/123',
    paperWidthMm: 58
  });
}
```

---

## 📍 Observações Técnicas

* Android: envia imagem renderizada como ESC/POS via Bluetooth
* iOS: usa renderização do sistema para AirPrint
* Layout e qualidade podem variar conforme a marca/modelo da impressora

---

## 📘 Roadmap Futuro

* [ ] Suporte a impressoras USB
* [ ] Impressão via IP (TCP/Socket)
* [ ] Exportação para PDF
* [ ] Impressão em background
* [ ] Interface nativa para seleção de impressora

---

## 🛠️ Desenvolvimento

```bash
npm run build     # compila o plugin
npx cap sync      # aplica mudanças no app
```

---

## 📄 Licença

MIT © ML Solutions
