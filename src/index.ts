import { registerPlugin } from '@capacitor/core';

import type { PrintThermalPlugin } from './definitions';

const PrintThermal = registerPlugin<PrintThermalPlugin>('PrintThermal', {
  web: () => import('./web').then((m) => new m.PrintThermalWeb()),
});

export * from './definitions';
export { PrintThermal };
