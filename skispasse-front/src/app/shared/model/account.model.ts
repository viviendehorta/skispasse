export class UserAccount {
  constructor(
    public activated: boolean,
    public authorities: string[],
    public email: string,
    public firstName: string,
    public lastName: string,
    public login: string,
  ) {}
}
