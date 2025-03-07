/*
 * Copyright (C) 2005 - 2020 TIBCO Software Inc. All rights reserved. Confidentiality & Proprietary.
 * Licensed pursuant to commercial TIBCO End User License Agreement.
 */

import _ from 'underscore';

export default function (columns) {
    var visibleColumns = _.filter(columns, function (column) {
        return column.interactive && column.visible;
    });

    return visibleColumns.length > 1;
}