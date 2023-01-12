/*
 * JavaScript Load Image Fetch
 * https://github.com/blueimp/JavaScript-Load-Image
 *
 * Copyright 2017, Sebastian Tschan
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * https://opensource.org/licenses/MIT
 */

/* global define, module, require, Promise */

;(function (factory) {
  'use strict'
  if (typeof define === 'function' && define.amd) {
    // Register as an anonymous AMD module:
    define(['./load-image'], factory)
  } else if (typeof module === 'object' && module.exports) {
    factory(require('./load-image'))
  } else {
    // Browser globals:
    factory(window.loadImage)
  }
})(function (loadImage) {
  'use strict'

  var global = loadImage.global

  if (
    global.fetch &&
    global.Request &&
    global.Response &&
    global.Response.prototype.blob
  ) {
    loadImage.fetchBlob = async function (urls, callback, options) {
      /**
       * Fetch response handler.
       *
       * @param {Response} response Fetch response
       * @returns {Blob} Fetched Blob.
       */
      async function responseHandler(response) {
        console.log(`responseHandler URL : ${response.url}, STATUS : ${response.status}`);
        return await response.blob();
      }
      if (global.Promise && typeof callback !== 'function') {
        const response_1 = await fetch(new Request(urls, callback))
          return responseHandler(response_1)
      }
      if (!isInstanceOf("Array", urls)) {
        fetch(new Request(urls, options))
          .then(responseHandler)
          .then(callback)
          [
            // Avoid parsing error in IE<9, where catch is a reserved word.
            // eslint-disable-next-line dot-notation
            'catch'
          ](function (err) {
            callback(null, err)
          });
      }
      else {
        let requests = urls.map(url => fetch(url));
        
        Promise.all(requests)
               .then(responses => Promise.all(responses.map(response => responseHandler(response)))
               							 .then(blobs => {for (let blob of blobs) callback(blob);}));
//               .then(blobs => {
//				  for (let blob of blobs) callback(blob);
//			   });
//               .then(blobs => Promise.all(blobs.map(blob => callback(blob))));
      }
    }
  } else if (
    global.XMLHttpRequest &&
    // https://xhr.spec.whatwg.org/#the-responsetype-attribute
    new XMLHttpRequest().responseType === ''
  ) {
    loadImage.fetchBlob = function (url, callback, options) {
      /**
       * Promise executor
       *
       * @param {Function} resolve Resolution function
       * @param {Function} reject Rejection function
       */
      function executor(resolve, reject) {
        options = options || {} // eslint-disable-line no-param-reassign
        var req = new XMLHttpRequest()
        req.open(options.method || 'GET', url)
        if (options.headers) {
          Object.keys(options.headers).forEach(function (key) {
            req.setRequestHeader(key, options.headers[key])
          })
        }
        req.withCredentials = options.credentials === 'include'
        req.responseType = 'blob'
        req.onload = function () {
          resolve(req.response)
        }
        req.onerror =
          req.onabort =
          req.ontimeout =
            function (err) {
              if (resolve === reject) {
                // Not using Promises
                reject(null, err)
              } else {
                reject(err)
              }
            }
        req.send(options.body)
      }
      if (global.Promise && typeof callback !== 'function') {
        options = callback // eslint-disable-line no-param-reassign
        return new Promise(executor)
      }
      return executor(callback, callback)
    }
  }
  
    /**
   * Cross-frame instanceof check.
   *
   * @param {string} type Instance type
   * @param {object} obj Object instance
   * @returns {boolean} Returns true if the object is of the given instance.
   */
  function isInstanceOf(type, obj) {
    // Cross-frame instanceof check
    return Object.prototype.toString.call(obj) === '[object ' + type + ']'
  }
})
