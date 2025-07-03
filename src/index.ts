import { registerPlugin } from '@capacitor/core';

import type { PrintThermalPluginPlugin } from './definitions';

const PrintThermalPlugin = registerPlugin<PrintThermalPluginPlugin>('PrintThermalPlugin', {
  web: () => import('./web').then((m) => new m.PrintThermalPluginWeb()),
});

export * from './definitions';
export { PrintThermalPlugin };
