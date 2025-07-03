export interface PrintThermalPluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
