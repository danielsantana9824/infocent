import dayjs from 'dayjs/esm';

export interface ICifrado {
  id?: number;
  nombre?: string;
  descripcion?: string;
  cifrado?: string;
  fechaVencimiento?: dayjs.Dayjs | null;
}

export class Cifrado implements ICifrado {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string,
    public cifrado?: string,
    public fechaVencimiento?: dayjs.Dayjs | null
  ) {}
}

export function getCifradoIdentifier(cifrado: ICifrado): number | undefined {
  return cifrado.id;
}
