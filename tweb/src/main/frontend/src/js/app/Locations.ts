export class Locations {
    public static requests = '/requests';
    public static requestDetail = '/requests/{id}';
    public static requestsUser = '/user/requests';

    public static gotoRequestDetail(id: string | number) {
        this._goto(this.requestDetail.replace('{id}', `${id}`));
    }

    private static _goto(url) {
        window.location.href = url;
    }
}