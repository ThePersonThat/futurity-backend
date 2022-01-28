package com.alex.futurity.projectserver.service;

import com.alex.futurity.projectserver.dto.*;

public interface ColumnManagerService {
    IdResponse createColumn(CreationColumnRequestDTO request);
    ListResponse<ColumnDTO> getColumns(TwoIdRequestDTO request);
    void deleteColumn(ThreeIdRequestDTO request);
    void changeIndexColumn(ChangeIndexColumnRequestDTO request);
}
