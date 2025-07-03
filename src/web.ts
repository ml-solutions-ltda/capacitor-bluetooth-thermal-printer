import { WebPlugin } from '@capacitor/core';

import type { PrintThermalPluginPlugin, ListPrintersResult, PrintOptions } from './definitions';

export class PrintThermalPluginWeb extends WebPlugin implements PrintThermalPluginPlugin {
  async listPrinters(): Promise<ListPrintersResult> {
    console.log('listPrinters');
    throw this.unimplemented('Not implemented on web.');
  }
  async print(options: PrintOptions): Promise<void> {
    console.log('print', options);
    throw this.unimplemented('Not implemented on web.');
  }
}
