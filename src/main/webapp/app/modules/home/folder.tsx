import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Col, Input, Row, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getSortState, JhiItemCount, JhiPagination, TextFormat } from 'react-jhipster';

import { APP_DATE_FORMAT, formatFileSize } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from 'app/entities/folder/folder.reducer';
import { IFile } from 'app/shared/model/file.model';
import { getAllEntitiesByFileId } from 'app/entities/file/file.reducer';
import FileDetailDialog from 'app/modules/home/file-detail-dialog';

export const Folder = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const folderList = useAppSelector(state => state.folder.entities);
  const loading = useAppSelector(state => state.folder.loading);
  const totalItems = useAppSelector(state => state.folder.totalItems);

  const [selectedFolderId, setSelectedFolderId] = useState<number | null>(null);
  const [fileList, setFileList] = useState<IFile[]>([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [originalFileList, setOriginalFileList] = useState<IFile[]>([]);
  const [fileSort, setFileSort] = useState({ sort: 'name', order: ASC });

  const [selectedFileId, setSelectedFileId] = useState<number | null>(null);
  const [isFileDetailModalOpen, setIsFileDetailModalOpen] = useState<boolean>(false);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  //Sort Folders
  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  //Handle Pagination for Folders
  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  //When folder item is clicked search the database for files with the corresponding fileId
  const handleFolderView = async (folderId: number) => {
    setSelectedFolderId(folderId);
    try {
      const response = await dispatch(getAllEntitiesByFileId({ id: folderId }));
      if (response.payload && Array.isArray((response.payload as any).data)) {
        setFileList((response.payload as any).data as IFile[]);
        setOriginalFileList((response.payload as any).data as IFile[]); // Store the original list
      } else {
        console.error('Empty response payload or incorrect format');
      }
    } catch (error) {
      console.error('Error fetching files:', error);
    }
  };

  //Determine what file type the different files have and display appropriate icon
  const getMediaTypeIcon = (typeName: string) => {
    switch (typeName) {
      case 'image':
        return 'png.png';
      case 'docx':
      case 'doc':
        return 'doc.png';
      case 'csv':
        return 'csv.png';
      case 'pdf':
        return 'pdf.png';
      case 'mp4':
        return 'mp4.png';
      default:
        return 'default.png'; // fallback icon
    }
  };

  //Live filter by typing into search bar
  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const query = event.target.value;
    setSearchQuery(query);

    if (query === '') {
      setFileList(originalFileList); // Reset to the original list
    } else {
      const filteredFiles = originalFileList.filter(file => file.name.toLowerCase().includes(query.toLowerCase()));
      setFileList(filteredFiles);
    }
    setSearchQuery(event.target.value);
  };

  useEffect(() => {
    const filteredFiles = fileList.filter(file => file.name.toLowerCase().includes(searchQuery.toLowerCase()));
    setFileList(filteredFiles);
  }, [searchQuery]);

  //Filter files by name, size and date added
  const sortFile = p => () => {
    const order = fileSort.sort === p && fileSort.order === ASC ? DESC : ASC;
    const sortedFiles = [...fileList].sort((a, b) => {
      if (a[p] < b[p]) return order === ASC ? -1 : 1;
      if (a[p] > b[p]) return order === ASC ? 1 : -1;
      return 0;
    });
    setFileList(sortedFiles);
    setFileSort({ sort: p, order });
  };

  //Open File Detail Dialog that containts details of the file
  const handleFileView = (fileId: number) => {
    setSelectedFileId(fileId);
    setIsFileDetailModalOpen(true);
  };

  const toggleFileDetailModal = () => {
    setIsFileDetailModalOpen(!isFileDetailModalOpen);
  };

  return (
    <div>
      <Row>
        <Col md="4">
          <h5 id="folder-heading" data-cy="FolderHeading">
            Folders
            <div className="d-flex justify-content-end">
              <Button className="me-2" color="primary" onClick={handleSyncList} disabled={loading}>
                <FontAwesomeIcon icon="sync" spin={loading} /> Refresh
              </Button>
            </div>
          </h5>
          <div className="table-responsive">
            {folderList && folderList.length > 0 ? (
              <Table responsive>
                <thead>
                  <tr>
                    <th className="hand" onClick={sort('id')}>
                      Id <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={sort('name')}>
                      Name <FontAwesomeIcon icon="sort" />
                    </th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {folderList.map((folder, i) => (
                    <tr key={`entity-${i}`} data-cy="entityTable">
                      <td>{folder.id}</td>
                      <td>{folder.name}</td>
                      <td className="text-end">
                        <div className="btn-group flex-btn-group-container">
                          <Button color="primary" size="sm" onClick={() => handleFolderView(folder.id)} data-cy="entityDetailsButton">
                            <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              !loading && <div className="alert bg-warning">No Folders found</div>
            )}
          </div>
          {totalItems ? (
            <div className={folderList && folderList.length > 0 ? '' : 'd-none'}>
              <div className="justify-content-center d-flex">
                <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
              </div>
              <div className="justify-content-center d-flex">
                <JhiPagination
                  activePage={paginationState.activePage}
                  onSelect={handlePagination}
                  maxButtons={5}
                  itemsPerPage={paginationState.itemsPerPage}
                  totalItems={totalItems}
                />
              </div>
            </div>
          ) : (
            ''
          )}
        </Col>
        <Col md="8">
          <div>
            <div>
              <h5 id="file-heading" data-cy="FileHeading">
                Files
                <div className="d-flex justify-content-end"></div>
              </h5>
            </div>
            <div>
              <div className="mb-3">
                <div className="search-bar">
                  <Input
                    type="text"
                    className="search-input"
                    name="search"
                    id="search"
                    placeholder="Search by file name"
                    value={searchQuery}
                    onChange={handleInputChange}
                  />
                </div>
              </div>

              {fileList && fileList.length > 0 ? (
                <div className="table-responsive">
                  <Table responsive>
                    <thead>
                      <tr>
                        <th>Media</th>
                        <th className="hand">
                          Folder
                        </th>
                        <th className="hand" onClick={sortFile('name')}>
                          Name <FontAwesomeIcon icon="sort" />
                        </th>
                        <th className="hand" onClick={sortFile('size')}>
                          Size <FontAwesomeIcon icon="sort" />
                        </th>
                        <th className="hand" onClick={sortFile('added')}>
                          Added <FontAwesomeIcon icon="sort" />
                        </th>
                        <th />
                      </tr>
                    </thead>
                    <tbody>
                      {fileList.map((file, index) => (
                        <tr key={index} data-cy="entityTable">
                          <td>
                            <img
                              src={`content/images/mediatypes/${getMediaTypeIcon(file.typeName)}`}
                              alt={file.typeName}
                              style={{ width: '24px', height: '24px' }}
                            />
                          </td>
                          <td>{file.foldername}</td>
                          <td>{file.name}</td>
                          <td>{formatFileSize(file.size)}</td>
                          <td>{file.added ? <TextFormat value={file.added} type="date" format={APP_DATE_FORMAT} /> : null}</td>
                          <td className="text-end">
                            <div className="btn-group flex-btn-group-container">
                              <Button color="primary" size="sm" onClick={() => handleFileView(file.id)}>
                                <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                              </Button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </Table>
                </div>
              ) : (
                <div className="alert bg-warning">No Files found</div>
              )}
            </div>
            <FileDetailDialog isOpen={isFileDetailModalOpen} toggle={toggleFileDetailModal} fileId={selectedFileId} />
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default Folder;
