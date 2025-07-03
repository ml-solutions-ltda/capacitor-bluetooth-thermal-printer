import { WebPlugin } from '@capacitor/core';

import type { PrintThermalPluginPlugin } from './definitions';

export class PrintThermalPluginWeb extends WebPlugin implements PrintThermalPluginPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
