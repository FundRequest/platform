import { Pipe, PipeTransform } from '@angular/core';
import {IRequestRecord} from '../../redux/requests.models';
import {Utils} from '../utils';

@Pipe({
  name: 'urlOfIssue'
})
export class UrlOfIssuePipe implements PipeTransform {

  transform(request: IRequestRecord): string {
      return request && request.issueInformation ? Utils.getUrlFromId(request.issueInformation.platform, request.issueInformation.platformId) : ''
  }

}
