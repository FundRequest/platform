import {Utils} from '../../shared/utils';
import {createNotification, INotificationRecord} from '../../redux/notifications.models';
import {createRequest, IRequestRecord} from '../../redux/requests.models';

interface FundDto {
  id: number;
  funder: string;
  amountInWei: string;
  amount: number;
  requestId: number;
  date: Date;
}

interface Message {
  id: number;
  date: Array<number>;
  type: string;
  requestDto: IRequestRecord,
  fundDto: FundDto,
}

export enum NotificationStreamType {
  REQUEST_CREATED = 'REQUEST_CREATED',
  REQUEST_FUNDED = 'REQUEST_FUNDED',
  REQUEST_CLAIMED = 'REQUEST_CLAIMED',
}

export class NotificationStreamMessage {


  private _notification: INotificationRecord;
  private _request: IRequestRecord = null;
  private _fund: FundDto = null;

  constructor(private _message: Message) {
    this._initNotification();
    this._initRequest();
    this._initFund();
  }

  private _initNotification(): void {
    this._notification = createNotification({
      id         : this._message.id,
      date       : Utils.dateTimeFromArray(this._message.date),
      type       : this._message.type,
      description: `Notification: ${this._message.type}`,
      link       : '',
      linkMessage: ''
    });
  }

  private _initRequest(): void {
    switch (NotificationStreamType[this._notification.type]) {
      case NotificationStreamType.REQUEST_CREATED:
      case NotificationStreamType.REQUEST_FUNDED:
        this._request = createRequest(this._message.requestDto);
        this._notification = this._notification.set('link', `/requests/${this._request.id}`);
        this._notification = this._notification.set('linkMessage', 'Go to request.');
        break;
    }

    switch (NotificationStreamType[this._notification.type]) {
      case NotificationStreamType.REQUEST_CREATED:
        let desc = `Request for "${this._request.issueInformation.title}" created.`;
        this._notification = this._notification.set('description', desc);
        break;
    }

  }

  private _initFund(): void {
    switch (NotificationStreamType[this._notification.type]) {
      case NotificationStreamType.REQUEST_FUNDED:
        this._fund = {
          id: this._message.fundDto.id,
          funder: this._message.fundDto.funder,
          amountInWei: this._message.fundDto.amountInWei,
          amount: Utils.fromWeiRounded(this._message.fundDto.amountInWei),
          requestId: this._message.fundDto.requestId,
          date: this._message.fundDto.date
        };
        let desc = `${this._fund.amount} FND was funded on "${this._request.issueInformation.title}".`;
        this._notification = this._notification.set('description', desc);
        break;
    }
  }

  public get request(): IRequestRecord {
    return this._request;
  }

  public get notification(): INotificationRecord {
    return this._notification;
  }
}
