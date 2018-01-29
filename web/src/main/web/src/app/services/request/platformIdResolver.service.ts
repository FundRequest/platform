import {Injectable} from '@angular/core';

import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/take';

@Injectable()
export class PlatformIdResolverService {

  public resolve(platform: string, issueLink: string): string {
    let matches = /^https:\/\/github\.com\/(.+)\/(.+)\/issues\/(\d+)$/.exec(issueLink);
    if (matches && matches.length >= 4) {
      return matches[1] + '|FR|' + matches[2] + '|FR|' + matches[3]
    } else {
      return null;
    }
  }


}
