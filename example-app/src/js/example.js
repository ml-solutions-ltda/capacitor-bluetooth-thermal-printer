import { PrintThermalPlugin } from '@capacitor-community/print-thermal';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    PrintThermalPlugin.echo({ value: inputValue })
}
