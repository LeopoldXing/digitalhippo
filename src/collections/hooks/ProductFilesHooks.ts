import { ProductFile } from "@/payload-types";

const uploadProductFile = async ({ doc }: { doc: ProductFile }) => {
  console.log(doc)
}

export { uploadProductFile }