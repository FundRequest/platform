import {IRequestRecord} from '../redux/requests.models';

declare let window: any;

export class Utils {
  public static openExternalUrl(url: string): void {
    let myWindow = window.open();
    myWindow.opener = null;
    myWindow.location = url;
  }

  public static fromWeiRounded(amountInWei: number | string): number {
    let number = +amountInWei / 1000000000000000000;
    return (Math.round(number * 100) / 100);
  }

  public static dateTimeFromArray(date: Array<number>): Date {
    return new Date(Date.UTC(date[0], date[1] - 1, date[2], date[3], date[4], date[5]));
  }

  public static getUrlFromId(platform: string, id: string): string {
    let idParts = id.split('|FR|');
    let repo = idParts[0];
    let project = idParts[1];
    let issue = idParts[2];
    let url = null;

    switch(platform) {
      // TODO: check others platforms as well
      case 'GITHUB':
        url = `https://github.com/${repo}/${project}/issues/${issue}`;
        break;
    }
    return url;
  }

  public static getUrlFromRequest(request: IRequestRecord): string {
    return this.getUrlFromId(request.issueInformation.platform, request.issueInformation.platformId);
  }

  public static getPlatformIdFromUrl(issueLink: string): string {
    let matches = /^https:\/\/github\.com\/(.+)\/(.+)\/issues\/(\d+)$/.exec(issueLink);
    if (matches && matches.length >= 4) {
      return matches[1] + '|FR|' + matches[2] + '|FR|' + matches[3]
    } else {
      return null;
    }
  }

  public static getPlatformFromUrl(issueLink: string): string {
    let matches = /^https:\/\/github\.com\/(.+)\/(.+)\/issues\/(\d+)$/.exec(issueLink);
    if (matches && matches.length >= 4) {
      return 'GITHUB';
    } else {
      return null;
    }
  }
}
