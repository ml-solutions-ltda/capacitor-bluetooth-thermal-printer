import { registerPlugin } from '@capacitor/core';

import type { PrintThermalPlugin } from './definitions';

const PrintThermalPlugin = registerPlugin<PrintThermalPlugin>('PrintThermalPlugin', {
  web: () => import('./web').then((m) => new m.PrintThermalPluginWeb()),
});

export * from './definitions';
export { PrintThermalPlugin };
