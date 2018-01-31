import {IRequestList, IRequestRecord} from '../redux/requests.models';
import {AbstractControl, ValidatorFn} from '@angular/forms';
import {Utils} from '../shared/utils';

export class CustomValidators {

  public static requestExists(requests: IRequestList): ValidatorFn {
    return (control: AbstractControl): { [key: string]: number | string } => {
      if (control.value != null && requests) {
        let url = control.value.trim();
        let platform = Utils.getPlatformFromUrl(url);
        let platformId = Utils.getPlatformIdFromUrl(url);
        let checkRequests = requests.filter((request: IRequestRecord) =>
          (request.issueInformation.platform == platform && request.issueInformation.platformId == platformId)
        );
        if (checkRequests.count() > 0) {
          return {'requestExists': checkRequests.first().id};
        }
      }

      return null;
    };
  }
}
