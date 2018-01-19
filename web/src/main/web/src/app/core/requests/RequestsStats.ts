export class RequestsStats {

  public requestsFunded: number;
  public numberOfFunders: number;
  public totalAmountFunded: number;
  public totalBalance: number;

  public get averageFundingPerRequest(): number {
    return this.totalAmountFunded / this.requestsFunded;
  }
}
