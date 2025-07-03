import { PrintThermalPlugin } from '@mlsolutions/print-thermal';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    PrintThermalPlugin.echo({ value: inputValue })
}
