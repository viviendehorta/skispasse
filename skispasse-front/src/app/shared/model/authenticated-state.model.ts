export class AuthenticatedState {
  constructor(
    public authenticated: boolean,
    public user: Account
  ) {}
}
