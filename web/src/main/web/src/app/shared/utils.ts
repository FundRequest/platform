export class Utils {
  public static fromWeiRounded(amountInWei: number | string): number {
    let number = +amountInWei / 1000000000000000000;
    return (Math.round(number * 100) / 100);
  }
}
