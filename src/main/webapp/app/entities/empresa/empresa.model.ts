import { ICifrado } from 'app/entities/cifrado/cifrado.model';

export interface IEmpresa {
  id?: number;
  nombre?: string;
  rif?: string;
  direccion?: string;
  telefono?: string;
  email?: string;
  cifrado?: ICifrado | null;
}

export class Empresa implements IEmpresa {
  constructor(
    public id?: number,
    public nombre?: string,
    public rif?: string,
    public direccion?: string,
    public telefono?: string,
    public email?: string,
    public cifrado?: ICifrado | null
  ) {}
}

export function getEmpresaIdentifier(empresa: IEmpresa): number | undefined {
  return empresa.id;
}
