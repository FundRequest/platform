import { Utils } from '../../shared/utils';
import { createNotification, INotificationRecord } from '../../redux/notifications.models';
import { createRequest, IRequestRecord } from '../../redux/requests.models';

interface Message {
  id: number;
  date: Array<number>;
  type: string;
  requestDto: IRequestRecord
}

export enum NotificationType {
  REQUEST_CREATED = "REQUEST_CREATED",
}

export class NotificationStreamMessage {


  private _notification: INotificationRecord;
  private _request: IRequestRecord = null;

  constructor(private _message: Message) {
    this._initNotification();
    this._initRequest();
  }

  private _initNotification(): void {
    this._notification = createNotification({
      id: this._message.id,
      date: Utils.dateTimeFromArray(this._message.date),
      type: this._message.type,
      description: `Notification: ${this._message.type}`
    });
  }

  private _initRequest(): void {
    if (NotificationType[this._notification.type] == NotificationType.REQUEST_CREATED) {
      this._request = createRequest(this._message.requestDto);
    }
  }

  public get request(): IRequestRecord {
    return this._request;
  }

  public get notification(): INotificationRecord {
    return this._notification;
  }
}
