"use strict";
function Notification() {}

Notification.prototype.notification = function(
  options,
  successCallback,
  errorCallback
) {
  options = options || {};
  options.successCallback = options.successCallback || successCallback;
  options.errorCallback = options.errorCallback || errorCallback;
  cordova.exec(
    options.successCallback || null,
    options.errorCallback || null,
    "Notification",
    "notification",
    [options]
  );
};

Notification.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.notification = new Notification();
  return window.plugins.notification;
};

cordova.addConstructor(Notification.install);
