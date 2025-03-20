import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination"

type Props = {
  current: number;
  totalPage: number;
  // eslint-disable-next-line no-unused-vars
  onPageChange: (page: number) => void;
}

const PaginationSelector = ({ current, totalPage, onPageChange }: Props) => {
  const pageNumber = [];

  for (let i = 1; i <= totalPage; i++) {
    pageNumber.push(i);
  }

  return (
      <Pagination>
        <PaginationContent>
          {current > 1 && (
              <PaginationItem>
                <PaginationPrevious href="#" onClick={() => onPageChange(current - 1)}/>
              </PaginationItem>
          )}
          <PaginationItem>
            {pageNumber.map(currentPage => (
                <PaginationLink href="#" onClick={() => onPageChange(currentPage)} isActive={currentPage === current} key={currentPage}>
                  {currentPage}
                </PaginationLink>))}
          </PaginationItem>
          <PaginationItem>
            <PaginationEllipsis/>
          </PaginationItem>
          {current < pageNumber.length && (
              <PaginationItem>
                <PaginationNext href="#" onClick={() => onPageChange(current + 1)}/>
              </PaginationItem>
          )}
        </PaginationContent>
      </Pagination>

  );
};

export default PaginationSelector;