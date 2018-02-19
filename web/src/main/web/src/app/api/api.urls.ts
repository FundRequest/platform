export class ApiUrls {
  private static _public: string = '/api/public';
  private static _private: string = '/api/private';

  /*
    Public api URL's
   */
  public static get requests(): string {
    return `${this._public}/requests`;
  }

  public static get qrFund(): string {
    return `${this._public}/requests/0/erc67/fund`;
  }

  public static get notificationStream(): string {
    return `${this._public}/notifications-stream`;
  }

  /*
    Private api URL's - User has to be logged in
   */
  public static get userInfo(): string {
    return `${this._private}/user/info`;
  }
  public static watchers(requestId: number): string {
    return `${this._private}/requests/${requestId}/watchers`;
  }

  public static canClaim(requestId: number): string {
    return `${this._private}/requests/${requestId}/can-claim`;
  }

  public static claim(requestId: number): string {
    return `${this._private}/requests/${requestId}/claim`;
  }
}
