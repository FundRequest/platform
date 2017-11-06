import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'wei'
})
export class WeiPipe implements PipeTransform {
  transform(amountInWei: number): string {
      let number = (amountInWei / 1000000000000000000);
      return ((Math.round(number * 100) / 100).toFixed(2)).toLocaleString();
  }
}
