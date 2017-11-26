export class Utils {
  public static fromWeiRounded(amountInWei: number | string): number {
    let number = +amountInWei / 1000000000000000000;
    return (Math.round(number * 100) / 100);
  }

  public static dateTimeFromArray(date: Array<number>): Date {
    return new Date(Date.UTC(date[0],date[1]-1,date[2],date[3]-1,date[4],date[5]));
  }
}
