import {IRequestList, IRequestRecord} from '../redux/requests.models';
import {AbstractControl, ValidatorFn} from '@angular/forms';

export class CustomValidators {

  public static requestExists(requests: IRequestList): ValidatorFn {
    return (control: AbstractControl): { [key: string]: number | string } => {
      if (control.value != null && requests) {
        let checkRequests = requests.filter((request: IRequestRecord) => request.issueInformation.link == control.value.trim());
        if (checkRequests.count() > 0) {
          return {'requestExists': checkRequests.first().id};
        }
      }

      return null;
    };
  }

  /*
  public static linkExists(httpClient: HttpClient): AsyncValidatorFn {
    return (control: AbstractControl): Promise<{ [key: string]: boolean | null }> => {
      if (control.value != null) {
        let headers: HttpHeaders = new HttpHeaders().set('Access-Control-Allow-Origin', '*');
        return httpClient.head(control.value, {headers: headers}).toPromise()
          .then(result => null)
          .catch(() => { return {'linkExists': false}; });
      }

      return Promise.resolve(null);
    };
  }*/
}
