(function() {
    'use strict';

    function generateUUID() { // Public Domain/MIT
        let d = new Date().getTime();
        if (typeof performance !== 'undefined' && typeof performance.now === 'function') {
            d += performance.now(); //use high-precision timer if available
        }
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            let r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
        });
    }

    chrome.extension.onMessage.addListener(
        function(request, sender, sendResponse) {
            let id = generateUUID();

            // let fundEvent = function(event) {
            //     window.removeEventListener('message', fundEvent);
            //     if (event.data.detail.id === id) {
            //         let finished = true;
            //         chrome.pageAction.show(sender.tab.id);
            //         sendResponse({done: true, detail: event.data.detail});
            //     }
            // };
            switch (request.action) {
                case 'load':
                    chrome.pageAction.show(sender.tab.id);
                    sendResponse();
                    break;
                case 'fund':
                    /*
                     * - Create a new tab with funding page
                     * -- Inject script to funding page
                     * -- Send message to new tab
                     * - When funding succeeded, close tab, add notification, reload funded button
                     * - When funding fails, close tab and add notification
                     */
                    chrome.tabs.create({url: "http://localhost:4200/#/requests/fund?url=" + encodeURI(request.url)}, function(tab) {

                        chrome.tabs.executeScript(tab.id, {file: "src/inject/fnd/script.js"}, function() {
                            chrome.tabs.sendMessage(tab.id, {id: id}, function(response) {
                                console.log('response from bg script', response);
                                response = typeof response !== 'undefined' ? response : {};
                                if (chrome.runtime.lastError) {
                                    response.done = false;
                                    response.message = chrome.runtime.lastError;
                                } else {
                                    response.done = true;
                                }
                                sendResponse(response);
                            });
                        });
                    });

                /*
                 let finished = false;
                 iframe.contentWindow.postMessage({action: 'fund', iframe: true, id: id}, '*');

                 window.addEventListener('message', fundEvent);

                 setTimeout(function() {
                 if (!finished) {
                 window.removeEventListener('message', fundEvent);
                 chrome.pageAction.show(sender.tab.id);
                 sendResponse({done: false});
                 }
                 }, 15000);
                 break;*/
            }
            return true;
        }
    );
})();