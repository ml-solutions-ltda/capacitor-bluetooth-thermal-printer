export interface Printer {
  name: string;
  address: string;
}

export interface PrintOptions {
  macAddress: string;
  html: string;
  logoBase64?: string;
  qrCodeText?: string;
  paperWidthMm?: number;
}

export interface Printer {
  name: string;
  address: string;
}

export interface ListPrintersResult {
  devices: Printer[];
}

export interface PrintThermalPlugin {
  listPrinters(): Promise<ListPrintersResult>;
  print(options: PrintOptions): Promise<void>;
}
