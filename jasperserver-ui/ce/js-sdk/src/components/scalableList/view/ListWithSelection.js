/*
 * Copyright (C) 2005 - 2020 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import $ from 'jquery';
import _ from 'underscore';
import ListWithSelectionModel from '../model/ListWithSelectionModel';
import ScalableList from './ScalableList';
var MOUSE_MOVE_MIN_PIXELS = 1;
var BUTTONS = {
    LEFT: 'left',
    RIGHT: 'right'
};
var ListWithSelection = ScalableList.extend({
    ListModel: ListWithSelectionModel,
    initialize: function (options) {
        _.bindAll(this, 'onGlobalMouseup', 'onGlobalMousemove', '_autoScroll', 'fetch');
        this.eventListenerPattern = options.eventListenerPattern || 'li';
        this.markerClass = options.markerClass ? options.markerClass : '';
        this.selectedClass = options.selectedClass ? options.selectedClass : 'jr-isSelected';
        this._initSelection(options.selection);
        return ScalableList.prototype.initialize.call(this, options);
    },
    delegateEvents: function () {
        var res = ScalableList.prototype.delegateEvents.apply(this, arguments);
        this.$el.on('mousedown', this.eventListenerPattern, _.bind(this.onMousedown, this)).on('mousemove', this.eventListenerPattern, _.bind(this.onMousemove, this)).on('dblclick', this.eventListenerPattern, _.bind(this.onMousedblclick, this)).on('contextmenu', this.eventListenerPattern, _.bind(this.onItemEvent, this, this.$el)).on('click', this.eventListenerPattern, _.bind(this.onItemEvent, this, this.$el)).on('mouseout', this.eventListenerPattern, _.bind(this.onItemEvent, this, this.$el)).on('mouseover', this.eventListenerPattern, _.bind(this.onItemEvent, this, this.$el));
        return res;
    },
    undelegateEvents: function () {
        var res = ScalableList.prototype.undelegateEvents.apply(this, arguments);
        this.$el.off('mouseup').off('mousedown').off('mousemove').off('dblclick').off('contextmenu').off('click').off('mouseout').off('mouseover');
        return res;
    },
    _initSelection: function (selection) {
        selection = _.extend({ allowed: {} }, selection);
        var selectionAllowed = selection.allowed, leftButtonSelection = !_.isObject(selectionAllowed) ? selectionAllowed : selectionAllowed.left;
        this.selection = {
            allowed: {
                left: typeof leftButtonSelection !== 'undefined' ? leftButtonSelection : true,
                right: typeof selectionAllowed.right !== 'undefined' ? selectionAllowed.right : false
            },
            multiple: typeof selection.multiple !== 'undefined' ? selection.multiple : true
        };
    },
    initListeners: function () {
        ScalableList.prototype.initListeners.call(this);
        this.listenTo(this.model, 'selection:clear', this.clearSelection, this);
        this.listenTo(this.model, 'selection:add', this.selectValue, this);
        this.listenTo(this.model, 'selection:addRange', this.selectRange, this);
        this.listenTo(this.model, 'selection:remove', this.deselectValue, this);
        $('body').on('mouseup', this.onGlobalMouseup).on('mousemove', this.onGlobalMousemove);
    },
    postProcessChunkModelItem: function (item, i) {
        ScalableList.prototype.postProcessChunkModelItem.call(this, item, i);
        item.selected = this.model.selectionContains && this.model.selectionContains(item.value, item.index);
    },
    onMousedblclick: function (event) {
        if (!this.selection.multiple) {
            if ((this.selection.allowed.left || this.selection.allowed.right) && !this.getDisabled()) {
                var itemData = this._getDomItemData(event.currentTarget);
                this._singleSelect(event, itemData.value, itemData.index);
                if (this.selectionChanged) {
                    this._triggerSelectionChanged();
                    this._triggerDblclicked();
                    this.selectionChanged = false;
                }
            }
        }
    },
    getItemByEvent: function (e) {
        return this._getDomItemData(e.currentTarget).item;
    },
    onItemEvent: function (element, e) {
        var eventType = e.type, item = this.getItemByEvent(e);
        this.trigger('list:item:' + eventType, item, e);
    },
    onMousedown: function (event) {
        if (!this.getDisabled() && (this.selection.allowed.left && event.which === 1 || this.selection.allowed.right && event.which === 3)) {
            var buttonType = event.which === 1 ? BUTTONS.LEFT : BUTTONS.RIGHT;
            if (this.selection.multiple) {
                this[buttonType + 'MouseButtonPressed'] = true;
                this.mouseDownPos = this._getMousePos(event);
            }
            var itemData = this._getDomItemData(event.currentTarget);
            this.selection.multiple ? this._multiSelect(event, itemData.value, itemData.index) : this._singleSelect(event, itemData.value, itemData.index);
        }
    },
    onMousemove: function (event) {
        if (this._allowMouseMoveSelection()) {
            this._stopAutoScroll();
            if (this._mouseMoved(event, this.mouseDownPos)) {
                this.mouseDownPos = this._getMousePos(event);
                var itemData = this._getDomItemData(event.currentTarget);
                if (!this.model.selectionContains(itemData.value, itemData.index)) {
                    this.model.addRangeToSelection(itemData.value, itemData.index);
                }
            }
        }
    },
    onGlobalMouseup: function (event) {
        var self = this;
        if (this.leftMouseButtonPressed || this.rightMouseButtonPressed) {
            _.each(BUTTONS, function (buttonType) {
                self[buttonType + 'MouseButtonPressed'] = false;
            });
            delete this.mouseDownPos;
            this._stopAutoScroll();
            if (this.selectionChanged) {
                this._triggerSelectionChanged();
                this.selectionChanged = false;
            }
        }
    },
    onGlobalMousemove: function (event) {
        if (this._allowMouseMoveSelection()) {
            this.mousePosY = event.clientY;
            if (!this.scrollInterval) {
                this.scrollInterval = setInterval(this._autoScroll, this.manualScrollInterval);
            }
        }
    },
    clearSelection: function () {
        this.$el.find('li.' + this.selectedClass + this.markerClass).removeClass(this.selectedClass);
        this.selectionChanged = true;
    },
    selectValue: function (selection) {
        this._selectItemByIndex(selection.index);
        if (this.selection.multiple) {
            this.selectionChanged = true;
        } else {
            this._triggerSelectionChanged();
        }
    },
    selectRange: function (options) {
        var self = this;
        var visibleRangeStart = Math.max(this.model.get('bufferStartIndex'), options.start);
        var visibleRangeEnd = Math.min(this.model.get('bufferEndIndex'), options.end);
        this.$el.find('li' + this.markerClass + ':not(.' + this.selectedClass + ')').each(function () {
            var $item = $(this);
            var index = parseInt($item.attr('data-index'), 10);
            if (index >= visibleRangeStart && index <= visibleRangeEnd) {
                var item = self.model.get('items')[index - self.model.get('bufferStartIndex')];
                if (self.model.selectionContains(item && item.value, index)) {
                    $item.addClass(self.selectedClass);
                }
            }
        });
        this.selectionChanged = true;
    },
    deselectValue: function (selection) {
        this._deselectItemByIndex(selection.index);
        if (this.selection.multiple) {
            this.selectionChanged = true;
        } else {
            this._triggerSelectionChanged();
        }
    },
    _selectItemByIndex: function (index) {
        this.$el.find('li' + this.markerClass + '[data-index=\'' + index + '\']:not(.' + this.selectedClass + ')').addClass(this.selectedClass);
    },
    _deselectItemByIndex: function (index) {
        this.$el.find('li[data-index=\'' + index + '\'].' + this.selectedClass + this.markerClass).removeClass(this.selectedClass);
    },
    _autoScroll: function () {
        var viewPortTop = this.$el.offset().top;
        var scrollTop = this.$el.scrollTop();
        var scrollPos = typeof this.scrollTop !== 'undefined' ? this.scrollTop : scrollTop;
        var newScrollPos;
        var shouldScroll = false;
        if (this.mousePosY < viewPortTop) {
            newScrollPos = scrollPos - this.itemHeight * 3;
            shouldScroll = newScrollPos < scrollTop;
        } else if (this.mousePosY > viewPortTop + this.viewPortHeight) {
            newScrollPos = scrollPos + this.itemHeight * 3;
            shouldScroll = newScrollPos > scrollTop;
        }
        if (shouldScroll) {
            this.$el.scrollTop(newScrollPos);
            this.scrollTop = this.$el.scrollTop();
            this._fetchVisibleData();
        }
        this._selectRangeOnAutoScroll(scrollPos, newScrollPos);
    },
    _selectRangeOnAutoScroll: function (oldScrollPos, newScrollPos) {
        var index;
        if (oldScrollPos < newScrollPos) {
            index = this._getVisibleItems().bottom;
        } else if (oldScrollPos > newScrollPos) {
            index = this._getVisibleItems().top;
        }
        if (index) {
            var item = this.model.get('items')[index - this.model.get('bufferStartIndex')];
            if (item) {
                if (!this.model.selectionContains(item.value, index)) {
                    this.model.addRangeToSelection(item.value, index);
                }
            }
        }
    },
    _stopAutoScroll: function () {
        if (this.scrollInterval) {
            clearInterval(this.scrollInterval);
            this.scrollInterval = undefined;
            this.scrollTop = undefined;
        }
    },
    _multiSelect: function (event, value, index) {
        if (event.shiftKey) {
            this.model.addRangeToSelection(value, index);
        } else if (event.ctrlKey || event.metaKey) {
            this._singleSelect(event, value, index);
        } else {
            this.model.toggleSelection(value, index);
        }
    },
    _singleSelect: function (event, value, index) {
        this.model.clearSelection().addValueToSelection(value, index);
    },
    _triggerSelectionChanged: function () {
        this.trigger('selection:change', this.getValue());
    },
    _triggerDblclicked: function () {
        this.trigger('item:dblclick', this.getValue());
    },
    _getDomItemData: function (item) {
        var $item = $(item);
        var index = $item.attr('data-index');
        while (index === undefined) {
            $item = $item.parent();
            index = $item.attr('data-index');
        }
        index = parseInt(index, 10);
        var modelItem = this.model.get('items')[index - this.model.get('bufferStartIndex')];
        return {
            item: modelItem,
            value: modelItem && modelItem.value,
            index: index
        };
    },
    _mouseMoved: function (event, mousePos) {
        return Math.abs(mousePos.x - event.clientX) + Math.abs(mousePos.y - event.clientY) >= MOUSE_MOVE_MIN_PIXELS;
    },
    _getMousePos: function (event) {
        return {
            x: event.clientX,
            y: event.clientY
        };
    },
    _allowMouseMoveSelection: function () {
        return (this.selection.allowed.left && this.leftMouseButtonPressed || this.selection.allowed.right && this.rightMouseButtonPressed) && this.selection.multiple && !this.getDisabled();
    },
    getValue: function () {
        return this.model.getSelection();
    },
    setValue: function (selection, options) {
        options = options || {};
        if ((!options || !options.silent) && (!options.modelOptions || !options.modelOptions.silent)) {
            this.model.once('selection:change', this._triggerSelectionChanged, this);
        }
        options = options.modelOptions;
        this.model.select(selection, options);
        return this;
    },
    selectAll: function (options) {
        if (!options || !options.silent) {
            this.model.once('selection:change', this._triggerSelectionChanged, this);
        }
        this.model.selectAll();
        return this;
    },
    selectNone: function (options) {
        return this.setValue({}, options);
    },
    invertSelection: function (options) {
        if (!options || !options.silent) {
            this.model.once('selection:change', this._triggerSelectionChanged, this);
        }
        this.model.invertSelection();
        return this;
    },
    setDisabled: function (disabled) {
        this.disabled = disabled;
        this.disabled ? this.$el.addClass('disabled') : this.$el.removeClass('disabled');
        return this;
    },
    getDisabled: function () {
        return this.disabled;
    },
    remove: function () {
        $('body').off('mouseup', this.onGlobalMouseup).off('mousemove', this.onGlobalMousemove);
        return ScalableList.prototype.remove.call(this);
    }
});
export default ListWithSelection;