import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'wei'
})
export class WeiPipe implements PipeTransform {
  transform(amountInWei: number | string): string {
    let number = (+amountInWei / 1000000000000000000);
    return ((Math.round(number * 100) / 100).toFixed(2)).toLocaleString();
  }
}

@Pipe({
  name: 'weiAsNumber'
})
export class WeiAsNumberPipe implements PipeTransform {
  transform(amountInWei: number | string): number {
    let number = (+amountInWei / 1000000000000000000);
    return Math.round(number * 100) / 100;
  }
}
