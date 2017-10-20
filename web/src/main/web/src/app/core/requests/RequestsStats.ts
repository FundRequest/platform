export class RequestsStats {

  public numberOfRequests: number;
  public requestsFunded: number;
  public numberOfFunders: number;
  public totalAmountFunded: number;
  public averageFundingPerRequest: number;

  public get percentageFunded(): number {
    return this.requestsFunded == 0 ? 0 : (this.numberOfRequests / this.requestsFunded);
  }

}
