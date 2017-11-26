import { makeTypedFactory, TypedRecord } from 'typed-immutable-record';
import { List } from 'immutable';

interface INotification {
  id: number;
  type: string;
  date: Date,
  description: string;
}


export interface INotificationRecord extends TypedRecord<INotificationRecord>, INotification {
}

export const createNotification = makeTypedFactory<INotification, INotificationRecord>({
  id         : 0,
  type       : '',
  date       : new Date(),
  description: '',
});

export type INotificationList = List<INotificationRecord>;
